import { Component, inject } from '@angular/core';
import { Header } from '../../../shared/header/header';
import { Router, RouterLink } from "@angular/router";
import { Logo } from '../../../shared/logo/logo';

@Component({
  selector: 'app-landing',
  imports: [Logo, RouterLink],
  templateUrl: './signup.html',
  styleUrl: './signup.scss',
})
export class SignUp {
  private readonly router=inject(Router)

  isDisabled:boolean=true

  role:string|null =null;
  selectRole(role:string|null){
    this.role=role;
    this.isDisabled=false
}
toSignup(){
  console.log(this.role)
  this.router.navigate([`/${this.role}s/signup`])
}

}
