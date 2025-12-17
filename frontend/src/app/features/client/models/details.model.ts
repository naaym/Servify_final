export interface Attachment{
  name:string;
  url:string;
}


 export interface TimelineStep {
  label:string;
  date:string;
}
export interface DetailsRequest {
  id: number;
  category: string;
  date: string;
  provider: {
    name: string;
    phone: string;
    rating: number;
  };
  status: string;
  description: string;
  attachments: Attachment[];
  timeline: TimelineStep[];
}

