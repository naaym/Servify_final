import { AbstractControl, ValidationErrors } from '@angular/forms';

export function passMatch(control:AbstractControl):ValidationErrors | null {
	const pass = control.get('password')?.value;
	const confpass=control.get('passconfirm')?.value;
	return pass===confpass ? null: {notmatch:true};
};



