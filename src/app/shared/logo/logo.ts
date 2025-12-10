import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-logo',
  imports: [],
  templateUrl: './logo.html',
  styleUrl: './logo.scss',
})

export class Logo {
   router=inject(Router)
  goHome() {
  this.router.navigate(['/']);
}

}
