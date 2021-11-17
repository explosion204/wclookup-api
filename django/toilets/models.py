from django.db import models


# Create your models here.
class Toilets(models.Model):
    toilet_id = models.IntegerField()
    address = models.TextField()
    serving = models.TextField()
    latitude = models.FloatField()
    longitude = models.FloatField()
    rating = models.FloatField()
    isConfirmed = models.BooleanField()


class Reviews(models.Model):
    review_id = models.IntegerField()
    userId = models.IntegerField()
    toiletId = models.IntegerField()
    rating = models.FloatField()
    text = models.TextField()


class Tickets(models.Model):
    ticket_id = models.IntegerField()
    topic = models.TextField()
    text = models.TextField()
    email = models.TextField()
    date = models.DateField()
    isChecked = models.BooleanField()
