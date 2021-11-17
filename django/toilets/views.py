from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import render, redirect
import requests
import json

# Create your views here.
from django.urls import reverse
from django.views import View

from toilets.models import Toilets


class ToiletInfo:
    def __init__(self, toilet_id, address, serving, latitude, longitude, rating, isConfirmed):
        self.toilet_id = toilet_id
        self.address = address
        self.serving = serving
        self.latitude = latitude
        self.longitude = longitude
        self.rating = rating
        self.isConfirmed = isConfirmed


class ReviewInfo:
    def __init__(self, review_id, userId, toiletId, rating, text, date):
        self.review_id = review_id
        self.userId = userId
        self.toiletId = toiletId
        self.rating = rating
        self.text = text
        self.date = date


class TicketInfo:
    def __init__(self, ticket_id, subject, text, email, date, Isresolved):
        self.ticket_id = ticket_id
        self.subject = subject
        self.text = text
        self.email = email
        self.date = date
        self.Isresolved = Isresolved


def index(request):
    return redirect(
        'https://accounts.google.com/o/oauth2/auth?client_id=478259788814-1tcm0270rib4nic6tivv96q5mdsikdvg.apps.googleusercontent.com&redirect_uri=http://localhost:8000/google&response_type=code&scope=email')


def google(request):
    code = request.GET['code']
    code = 'https://www.googleapis.com/oauth2/v4/token?client_id=478259788814-1tcm0270rib4nic6tivv96q5mdsikdvg.apps.googleusercontent.com&client_secret=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&redirect_uri=http://localhost:8000/google&code=' + code + '&grant_type=authorization_code'
    response = requests.post(code).content
    response = json.loads(response)
    headers = {'Google-Access-Token': response['id_token']}
    response = requests.post('https://wclookup.online/api/auth/authenticate', headers=headers)
    http_response = HttpResponseRedirect("toilets")
    response = json.loads(response.content)
    http_response.set_cookie('accessToken', value=response['accessToken'])
    http_response.set_cookie('refreshToken', value=response['refreshToken'])

    return http_response


class ToiletView(View):
    # template_name = 'toilets/toilets.html'

    def get(self, request):
        header = {'Access-Token': request.COOKIES['accessToken']}
        allt = requests.get('https://wclookup.online/api/toilets?page=1&pagesize=10&latitude=0&longitude=0',
                            headers=header)
        allt = json.loads(allt.content)
        toilets = []
        for entity in allt['data']:
            toilets.append(
                ToiletInfo(entity['id'], entity['address'], entity['schedule'], entity['latitude'], entity['longitude'],
                           entity['rating'], entity['confirmed']))
        return render(request, 'toilets/toilets.html', {"toilet_list": toilets})

    # def delete(self, request, id):
    #     header = {'Access-Token': request.COOKIES['accessToken']}
    #     request.delete('https://wclookup.online/api/toilets/' + str(id), headers=header)


class ReviewView(View):

    def get(self, request, value):
        header = {'Access-Token': request.COOKIES['accessToken']}
        allt = requests.get(
            'https://wclookup.online/api/reviews?toiletId=' + str(value) + '&hours=49&page=1&pageSize=1',
            headers=header)
        allt = json.loads(allt.content)
        reviews = []
        for entity in allt['data']:
            reviews.append(
                ReviewInfo(entity['id'], entity['userId'], entity['toiletId'], entity['rating'], entity['text'],
                           entity['creationTime']))
        return render(request, 'toilets/reviews.html', {"reviews_list": reviews})


class TicketsView(View):
    def get(self, request):
        header = {'Access-Token': request.COOKIES['accessToken']}
        allt = requests.get('https://wclookup.online/api/tickets?page=1&size=10', headers=header)
        allt = json.loads(allt.content)
        tickets = []
        for entity in allt['data']:
            tickets.append(
                TicketInfo(entity['id'], entity['subject'], entity['text'], entity['email'], entity['creationTime'],
                           entity['resolved']))
        return render(request, 'toilets/tickets.html', {"ticket_list": tickets})


class ReplyView(View):
    def get(self, request, value):
        header = {'Access-Token': request.COOKIES['accessToken']}
        entity = requests.get('https://wclookup.online/api/tickets/' + str(value), headers=header)
        entity = json.loads(entity.content)
        ticket = TicketInfo(entity['id'], entity['subject'], entity['text'], entity['email'], entity['creationTime'],
                            entity['resolved'])
        return render(request, 'toilets/reply.html', {"ticket": ticket})


class EditView(View):
    def get(self, request, value):
        header = {'Access-Token': request.COOKIES['accessToken']}
        entity = requests.get('https://wclookup.online/api/toilets/' + str(value), headers=header)
        entity = json.loads(entity.content)
        toilet = ToiletInfo(entity['id'], entity['address'], entity['schedule'], entity['latitude'],
                            entity['longitude'],
                            entity['rating'], entity['confirmed'])
        return render(request, 'toilets/create_or_edit.html', {"toilet": toilet})
