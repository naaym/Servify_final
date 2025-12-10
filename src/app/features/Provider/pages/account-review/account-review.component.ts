import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-account-review',
  imports: [],
  templateUrl: './account-review.html',
  styleUrl: './account-review.scss',
})
export class AccountReview implements OnInit {
  router = inject(Router);
  id!:number;
  status!:string;
  message!:string;
  ngOnInit(): void {

    const nav = this.router.getCurrentNavigation();
   const data = nav?.extras.state;

  if (data) {
     this.id = data['id'];
    this.status = data['status'];
  }
  }
  toHome(){
    this.router.navigate(['/'])
  }


}
