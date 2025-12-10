import { Component, inject, OnInit } from '@angular/core';
import { ProviderCardComponent } from '../components/provider-card.component/provider-card.component';
import { ActivatedRoute, Router } from '@angular/router';
import { searchProviderService } from '../services/provider-search.service';
import { SearchProviderRequest } from '../models/relsult-search.model';
import { Provider } from '../models/provider.model';
import { Footer } from '../../../shared/footer/footer';
import { Header } from '../../../shared/header/header';

@Component({
  selector: 'app-search-results.component',
  imports: [ProviderCardComponent,Footer,Header],
  templateUrl: './search-results.component.html',
  styleUrl: './search-results.component.scss',
})
export class SearchResultsComponent implements OnInit {
  activatedRoute=inject(ActivatedRoute)
  search=inject(searchProviderService)
  router=inject(Router);
  // initialisation mte3 searchProviderRequest
  totalPages:number=3
  service:string=""
  city:string="";
  minPrice!: number ;
  maxPrice!: number;
  rate!: number;
  sort!:string;
  page:number=0;
  size:number=10;
  providerInfoResults!:Provider[]
  //----------------------------------
  ngOnInit(): void {
    this.service=this.activatedRoute.snapshot.queryParamMap.get("service") || "";
    this.city=this.activatedRoute.snapshot.queryParamMap.get("city") || "";
    this.fetchResults();

  }


fetchResults(){
      const request:SearchProviderRequest ={service : this.service,
        governorate:this.city,
        minPrice:this.minPrice,
        maxPrice:this.maxPrice,
        rate:this.rate,
        skills:this.selectedSkills,
        sort:this.sort,
        page:this.page,
        size:this.size}
        console.log(request)
         this.search.searchProvider(request).subscribe({next:(resp)=>{
           this.providerInfoResults=resp.provider;
           console.log(resp)}})
      }


      showProviderDetails(id:number){
        this.router.navigate(["/search/providers",id])

  }
  newBooking(id:number){
    this.router.navigate(["/providers",id,"booking"])
  }
  //extraction mte3 les donnes me template

  onMinPriceChange(event:Event){
    const input = event.target as HTMLInputElement
    this.minPrice= +input.value

    this.fetchResults()

  }
  onMaxPriceChange(event:Event){
    const input = event.target as HTMLInputElement
    this.maxPrice= +input.value
    this.fetchResults()
  }
  onRatingChange(rate:string){
    this.rate=+rate
    this.fetchResults();
  }
  onSortChange(event:Event){
    const input=event.target as HTMLInputElement
    this.sort=input.value;
    this.fetchResults()
  }
  resetFilters(){
      this.sort = "rating,desc";
  this.page = 0;
  this.size = 10;

  this.minPrice =0
  this.maxPrice = 0
  this.rate = 0
  this.selectedSkills = [];
  this.fetchResults()

  }
  //---------------------------
  nextPage(){
    this.page++
    this.fetchResults()
  }
  prevPage(){
    this.page--
    this.fetchResults()

  }
  //-------------- pour les skills selectionness
  selectedSkills:string[]=[];
  disabledSkills:{[key:string]:boolean}={};
  toggleSkill(skill:string){
      this.disabledSkills[skill]=true
    this.selectedSkills.push(skill);
    this.fetchResults();
  }



























  // mock test
  providers = [
    {
      providerId: 10,
      name: 'iheb hkiri',
      service: 'SWE',
      governorate: 'Ariana',
      delegation:'bir el bay',
      reviewsCount: 20,
      price: 50,
      imageProviderUrl: 'kjsldfdf',
      skills: ['pentesting'],
      rate: 4.9,
    },
    {
      providerId: 30,
      name: 'fedi bazzi',
      service: 'winou',
      governorate: 'jandouba',
      delegation:'bir el bay',
      reviewsCount: 20,
      price: 50,
      imageProviderUrl: 'kjsldfdf',
      skills: ['web dev '],
      rate: 4.9,
    },
  ];
}


