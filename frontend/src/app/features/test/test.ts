import { Component, inject, input, Input, NgModule } from '@angular/core';
import { Router } from '@angular/router';
import {
  FormBuilder,
  FormsModule,
  NgModel,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule, NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-test',
  imports: [FormsModule, ReactiveFormsModule,CommonModule],
  templateUrl: './test.html',
})
export class Test {

   booking= {
  id: 42,
  service: "WEB DEVELOPMENT",
  providerName: "EL 3ISSEWI",
  providerImage: "https://randomuser.me/api/portraits/men/76.jpg",
  providerLocalisation: "Ariana",
  date: "2025-11-27",
  time: "15:00",
  description: "Création d'un site web responsive avec une landing page et un dashboard admin.",
  status: "PENDING",
  attachments: [
    { url: "https://via.placeholder.com/300x200?text=Mock+Image+1" },
    { url: "https://via.placeholder.com/300x200?text=Mock+Image+2" },
    { url: "https://via.placeholder.com/300x200?text=Mock+Image+3" }
  ]
};



  constructor(private router: Router) {}



  previews: string[] = [];
  http = inject(HttpClient);
  response?: any;
  selectedFiles: File[] = [];

  onFilesSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    const allowedTypes = ['image/jpeg', 'image/png'];
    const maxSize = 5 * 1024 * 1024; // 5MB
    if (!input.files || input.files.length === 0) return;

    this.selectedFiles = Array.from(input.files);
    this.selectedFiles=this.selectedFiles.filter(file => {
      if (!allowedTypes.includes(file.type)) {
        alert(`${file.type} extension not supported`);
        return false;
      }
      if(file.size>maxSize){
        alert("fichier DOIT ETRE < 5 MB");
        return false;
      }
      return true
    });


    this.previews = []; // pour la reinistialisation

    const reader = new FileReader();
    this.selectedFiles.forEach((file) => {
      reader.onload = () => this.previews.push(reader.result as string);
      reader.readAsDataURL(file);
    });
  }
  fb = inject(FormBuilder);
  form = this.fb.nonNullable.group({
    nom: ['', Validators.required],
    prenom: ['', Validators.required],
  });
  selectedFile?: File[];


  onSubmit() {
    if (this.form.invalid) {
      console.log('form unsubmitted');
      return;
    }
    console.log('form submitted');
    const formdata = new FormData();
    formdata.append('name', this.form.controls.nom.value);
    formdata.append('lastname', this.form.controls.prenom.value);
    this.selectedFiles.forEach((file) => formdata.append('images', file));
    this.http.post('/api/sendfiles', formdata).subscribe({
      next: (res) => (this.response = res),
      error: (err) => console.log('error ', err),
    });
  }
  removeFile(index:number){
    this.selectedFiles.splice(index, 1);
  this.previews.splice(index, 1);
  }


  //------------------------------------------------- fct test me tasla7ech
  onselect(event:Event){
   const  inputs= event.target as HTMLInputElement;
    console.log(inputs.value)
  }

}
