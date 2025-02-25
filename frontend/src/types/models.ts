export enum UserRole {
  ADMIN = 'ADMIN',
  SALES_MANAGER = 'SALES_MANAGER',
  SALES_REPRESENTATIVE = 'SALES_REPRESENTATIVE',
  READ_ONLY = 'READ_ONLY'
}

export enum CustomerStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  LEAD = 'LEAD'
}

export enum CustomerCategory {
  REGULAR = 'REGULAR',
  VIP = 'VIP',
  PREMIUM = 'PREMIUM'
}

export enum CommunicationPreference {
  EMAIL = 'EMAIL',
  PHONE = 'PHONE',
  BOTH = 'BOTH'
}

export interface User {
  id: number
  username: string
  email: string
  roles: UserRole[]
  createdAt: string
  updatedAt: string
}

export interface Customer {
  id: number
  name: string
  email: string
  phone: string
  status: CustomerStatus
  category: CustomerCategory
  createdBy: number
  createdAt: string
  updatedAt: string
}

export interface Contact {
  id: number
  customerId: number
  name: string
  email: string
  phone: string
  position: string
  communicationPreference: CommunicationPreference
  createdBy: number
  createdAt: string
  updatedAt: string
}

export interface UserFormData {
  username: string
  email: string
  password?: string
  roles: UserRole[]
}

export interface CustomerFormData {
  name: string
  email: string
  phone: string
  status: CustomerStatus
  category: CustomerCategory
}

export interface ContactFormData {
  name: string
  email: string
  phone: string
  position: string
  communicationPreference: CommunicationPreference
}
