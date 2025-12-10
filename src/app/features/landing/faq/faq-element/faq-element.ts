import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-faq-element',
  imports: [],
  templateUrl: './faq-element.html',
  styleUrl: './faq-element.scss',
})
export class FaqElement {
  isOpen: boolean = false;


  @Input({required:true})question: string = '';
  @Input({required:true})answer: string = ``;


  toggle(): void {
    this.isOpen = !this.isOpen;
  }

}
