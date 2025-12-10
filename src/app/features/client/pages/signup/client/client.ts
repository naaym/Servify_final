import { Component, inject } from '@angular/core';
import { Logo } from '../../../../../shared/logo/logo';
import {
  FormBuilder,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ClientSignUpService } from '../../../services/client-signup.service';
import { passMatch } from '../../../../../shared/validators/pass-match.validator';
import { Router, RouterLink } from '@angular/router';
import { ShowMessageService } from '../../../../../shared/services/showmessage.service';


@Component({
  selector: 'app-client',
  imports: [Logo, ReactiveFormsModule, RouterLink],
templateUrl: './client.html',
  styleUrl: './client.scss',
})
export class Client {
  private readonly clientsignupservice = inject(ClientSignUpService);
  private readonly fb = inject(FormBuilder);
  private readonly showmessage=inject(ShowMessageService)
   router = inject(Router)

  form = this.fb.nonNullable.group({
    name: ['', [Validators.required]],
    governorate: ['', [Validators.required]],
    phone: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    passconfirm: ['', [Validators.required, Validators.minLength(6)]],// mehich gue3da temchi

  },{validators:passMatch});

  onSubmit() {
     if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;

    }
    console.log("form submitted")
     const payload = this.form.getRawValue()
     this.clientsignupservice.register(payload).subscribe({next:(value)=>{this.showmessage.show("success",value.message)

      this.router.navigate(["/login"]);
     },
      error:err=>this.showmessage.show('error',err.message)})

  }

  isInvalid(controlName: string): boolean {
    const control = this.form.get(controlName);
     if (!control) return false; // ken controlName 8alet ou n"existe pas donc le chaamp ==> null
    return control.invalid && ( control.dirty||control.touched);
  }

}
