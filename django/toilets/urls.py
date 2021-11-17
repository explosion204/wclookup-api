from django.conf.urls import url
from django.urls import include

from . import views

urlpatterns = [
    url("google", views.google),
    url("auth", views.index),
    url("toilets", views.ToiletView.as_view(), name='toilets'),
    url("tickets", views.TicketsView.as_view(), name='tickets'),
    url("reply/(?P<value>\d+)", views.ReplyView.as_view(), name='reply'),
    url("reviews/(?P<value>\d+)", views.ReviewView.as_view(), name='reviews'),
    url("edit/(?P<value>\d+)", views.EditView.as_view(), name='edit')
]