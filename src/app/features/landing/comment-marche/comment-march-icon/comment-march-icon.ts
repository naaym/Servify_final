import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-comment-marche-icon',
  imports: [],
  templateUrl: './comment-march-icon.html',
  styleUrl: './comment-march-icon.scss',
})
export class CommentMarchIcon {
  @Input({required:true}) image!:{src:string,alt:string};
  @Input({required:true}) titre!:string;
  @Input({required:true}) description!:string;

}
