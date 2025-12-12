import { Component, inject, input } from '@angular/core';
import { Logo } from '../../../../../shared/logo/logo';
import { ReactiveFormsModule, Validators, FormBuilder } from '@angular/forms';
import { passMatch,} from '../../../../../shared/validators/pass-match.validator';
import { isMissingFile} from '../../../../../shared/validators/filevalidation.validator';
import { ProviderSignUpService } from '../../../services/provider-signup.service';
import { ProviderSignUpRequest } from '../../../models/provider-signup.model';
import { ShowMessageService } from '../../../../../shared/services/showmessage.service';
import { Router } from '@angular/router';
import { ArrowBigLeft, ArrowBigRight, LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-prestataire',
  imports: [Logo, ReactiveFormsModule,LucideAngularModule],
  templateUrl: './provider.html',
  styleUrl: './prestataire.scss',
})
export class Provider {
  private readonly fb = inject(FormBuilder);
  private readonly providersignupservice = inject(ProviderSignUpService);
  private readonly showMessage = inject(ShowMessageService);
  private readonly router = inject(Router);
  arrowr=ArrowBigRight
  arrowl=ArrowBigLeft
  step:number=1
   nextStep(){
    this.step++
   }
   backStep(){
    this.step--
   }
  form = this.fb.nonNullable.group(
    {
      name: ['', Validators.required],
      email: ['', [Validators.email, Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      passconfirm: ['', [Validators.required, Validators.minLength(6)]],

      cin: [null, Validators.required],
      cv: [null, Validators.required],
      diplome: [null, Validators.required],
      phone: ['', Validators.required],
      governorate: ['', Validators.required],
      delegation: ['', Validators.required],
      age: ['', [Validators.required, Validators.pattern('^\\d+$')]],
    },{ validators: [passMatch] }

  );

  isValid(control: string): boolean {
    const input = this.form.get(control);
    if (!input) return false;
    return input.invalid && (input.touched || input.dirty);
  }
  //validation mte3 el file
  //------------------------------------------------------------------
  allowedTypes = ['application/pdf'];
  maxSize = 5 * 1024 * 1024; // 5MB

  onFileSelected(event: Event, controlName: string) {
  const input = event.target as HTMLInputElement;
  const files = input.files;

  if (!files || files.length === 0) return;

  const file = files[0];

  // Vérifie l'extension PDF
  if (!this.allowedTypes.includes(file.type)) {
    alert("Only PDF files are allowed");
    this.form.patchValue({ [controlName]: null });
    return;
  }

  // Vérifie la taille (5MB)
  if (file.size > this.maxSize) {
    alert("File must be less than 5MB");
    this.form.patchValue({ [controlName]: null }); // ← SAFE RESET
    return;
  }

  // mise à jour du form control
  this.form.patchValue({ [controlName]: file });
}
  //---------------------------------------------------------------------------

  private buildRequest(): ProviderSignUpRequest {
    return {
      name: this.form.value.name!,
      email: this.form.value.email!,
      phone: this.form.value.phone!,
      password: this.form.value.password!,
      governorate: this.form.value.governorate!,
      delegation: this.form.value.delegation!,
      age: Number(this.form.value.age),
      cin: this.form.value.cin!,
      cv: this.form.value.cv!,
      diplome: this.form.value.diplome!,
    };
  }
  data!: ProviderSignUpRequest;

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      alert("Some fields are wrongs ");
      return;
    }
    console.log('form submitted ');
    this.data = this.buildRequest();
    this.providersignupservice
      .signUp(this.data)
      .subscribe({
        next: (res) =>{ this.showMessage.show('info', res.message)

          this.router.navigate(['/providers/account-review'], { state: { id: res.providerId, status: res.status } })
          ;
        },
        error: err => {
          this.showMessage.show('error', err.message  );
    } });

  }
}
