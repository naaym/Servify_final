import { Injectable } from "@angular/core";

@Injectable({providedIn:"root"})
export class AttachmentService{

   allowedTypes=["image/jpeg","image/png"]
   maxSizeAllowed=5*1024*1024
   onUploadAttachments(selectedFiles:File[],preview:string[]){


    selectedFiles=selectedFiles.filter(file=>{
      if(!this.allowedTypes.includes(file.type)){
      alert(`${file.type}  not supported`)
       return false }
       if(file.size>this.maxSizeAllowed){
        console.log("file size should be under 5 Mb")
        return false
       }
      return true})
      preview=[]
       for (const file of selectedFiles) {
    const reader = new FileReader();
    reader.onload = () => {
      preview.push(reader.result as string);
    };
    reader.readAsDataURL(file);
  }
  return preview 
  }
}
