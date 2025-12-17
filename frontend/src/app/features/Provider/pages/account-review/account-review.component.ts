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
  id!: number;
  status!: string;

  ngOnInit(): void {
    const data = history.state;// 7lowa de plus persistante si je fais refresh 

    if (data && data.id) {
      this.id = data.id;
      this.status = data.status;
    } else {
      this.router.navigate(['/']);
    }
  }

  toHome() {
    this.router.navigate(['/']);
  }
}
