import { AbstractControl, ValidationErrors } from "@angular/forms";

export function isMissingFile(group: AbstractControl): ValidationErrors | null  {

    const cin = group.value;
    const cv = group.value;
    const diplome = group.value;
     return (!cin && !cv&& !diplome) ? null: {fileMissing:true}

  };
