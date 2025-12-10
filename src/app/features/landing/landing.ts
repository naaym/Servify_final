import { Component } from '@angular/core';
import { Header } from '../../shared/header/header';
import { Footer } from '../../shared/footer/footer';
import { HeroSection } from './hero-section/hero-section';
import {Service} from './catlogue-service/service'
import { CommentMarche } from './comment-marche/comment-marche';
import { Faq } from './faq/faq';
import { Temoignage } from './temoignage/temoignage';

@Component({
  selector: 'app-landing',
  imports: [Header,Footer,HeroSection,Service,CommentMarche,Faq,Temoignage],
  templateUrl: './landing.html',
  styleUrl: './landing.scss',
})
export class Landing {

}
