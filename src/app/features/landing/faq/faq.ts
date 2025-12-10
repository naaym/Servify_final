import { Component } from '@angular/core';
import { FaqElement } from './faq-element/faq-element';

@Component({
  selector: 'app-faq',
  imports: [FaqElement],
  templateUrl: './faq.html',
  styleUrl: './faq.scss',
})
export class Faq {

}
