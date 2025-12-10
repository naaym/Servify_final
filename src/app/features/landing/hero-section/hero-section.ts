import { UpperCasePipe } from '@angular/common';
import { Component, inject } from '@angular/core';
import { LucideAngularModule, SearchIcon } from 'lucide-angular';
import { ServiceModal } from '../modals/service.modal/service.modal';
import { CityModal } from '../modals/city.modal/city.modal';
import { Router } from '@angular/router';
import { SearchStateService } from '../../../shared/services/serachstate.service';
import { HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-hero-section',
  imports: [LucideAngularModule, ServiceModal, CityModal],
  templateUrl: './hero-section.html',
  styleUrl: './hero-section.scss',
})
export class HeroSection {
  router = inject(Router);
  searchcontext=inject(SearchStateService)
  icon = SearchIcon;

  isServiceModalOpen: boolean = false;
  isCityModalOpen: boolean = false;
  nameService!: string;


  backToServiceModal(){
    this.toggleCityModal(false);
    this.toggleServiceModal(true)
  }


  toggleServiceModal(open:boolean){
    this.isServiceModalOpen=open;
  }
  toggleCityModal(open:boolean){
    this.isCityModalOpen=open;
  }

  onServiceChosen(service: string) {
    this.nameService = service;
    this.toggleServiceModal(false)
    this.toggleCityModal(true);
    console.log(this.nameService);
  }


  onCityChosen(city: string) {
    this.searchcontext.setSearchContext(city,this.nameService)
     this.router.navigate(['/search'], {
    queryParams: {
      service: this.nameService,
      city: city
    }
  })


  }
}
