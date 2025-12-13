import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Logo } from '../../../shared/logo/logo';
import { AuthService } from '../services/auth.service';
import { ShowMessageService } from '../../../shared/services/showmessage.service';

@Component({
  selector: 'app-loginclient',
  imports: [ReactiveFormsModule, Logo, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  private readonly auth = inject(AuthService);
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly showmessage = inject(ShowMessageService);

  form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
  });

  onSubmit() {
    const payload = this.form.getRawValue();

    this.auth.login(payload).subscribe({
      next: (val) => {
        switch (val.role) {
          case 'ADMIN':
            this.router.navigate(['/admins/dashboard']);
            break;

          case 'PROVIDER':
            switch (val.status) {
              case 'ACCEPTED':
                this.showmessage.show('success', val.message);
                this.router.navigate(['/providers/dashboard']);

                break;

              case 'REJECTED':
                this.showmessage.show('error', val.message);
                this.router.navigate(['/providers/rejected']);
                break;

              case 'PENDING':
               this.router.navigate(['/providers/account-review'], { state: { id: val.id, status: val.status } })



                break;
            }
            break;

          case 'CLIENT':
            this.router.navigate(['/clients/dashboard']);
            break;

          default:
            this.router.navigate(['/']);
        }
      },
      error: (err) => this.showmessage.show('error', err.message),
    });
  }

  isInvalid(controlName: string): boolean {
    const control = this.form.get(controlName);
    if (!control) return false; // ken controlName 8alet ou n"existe pas donc le chaamp ==> null
    return control.invalid && (control.dirty || control.touched);
  }
}
