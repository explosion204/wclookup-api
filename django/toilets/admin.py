from django.contrib import admin

# Register your models here.
from toilets.models import Toilets, Reviews, Tickets

admin.site.register(Toilets)
admin.site.register(Reviews)
admin.site.register(Tickets)
