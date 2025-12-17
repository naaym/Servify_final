import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface UploadedImage {
  file: File;
  preview: string;
}

@Component({
  selector: 'app-client-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html'
})
export class ProfileComponent {

  // Fake data -> remplacé par API Spring Boot après
  user = {
    name: 'Oussama Hkiri',
    email: 'oussama@example.com',
    phone: '+216 22 222 222',
    gouvernorat: 'Tunis',
    address: 'Cité Ennasr 2',
    notifications: true,
    darkMode: false
  };

  profileImage: UploadedImage | null = null;

  updateProfile() {
    console.log('PROFILE UPDATED:', this.user);
  }

  updateSecurity() {
    console.log('SECURITY UPDATED');
  }

  onImageChange(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      this.profileImage = {
        file,
        preview: reader.result as string
      };
    };
    reader.readAsDataURL(file);
  }
}
