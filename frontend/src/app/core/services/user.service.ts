import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { currentUser } from '../models/user.model';

@Injectable({ providedIn: 'root' })

export class UserService {
  private _user = new BehaviorSubject<currentUser|null>(null);

  user$=this._user.asObservable();


  setUser(user:currentUser){
this._user.next(user);
  }

  getUser():currentUser|null{
    return this._user.value;
  }

  clearUser(){

    this._user.next(null);

  }






}
