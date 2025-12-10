import { Injectable } from "@angular/core";

@Injectable({providedIn:"root"})
export class SearchStateService {

  selectedService!:string;
  selectedCity!:string;
  setSearchContext(city:string,service:string){
    this.selectedCity=city;
    this.selectedService=service;
  }

}
