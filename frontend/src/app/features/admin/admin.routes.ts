import { Routes } from "@angular/router";
import { DashboardAdmin } from "./pages/dashboard-admin/dashboard-admin.component";
import { AdminChatsComponent } from "../admin-chat/admin-chat.component";

export const ADMIN_ROUTS:Routes =[
  {path:"dashboard", component:DashboardAdmin},
  {path:"chats", component:AdminChatsComponent}





]
