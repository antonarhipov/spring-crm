import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'

interface Contact {
  id: number
  customerId: number
  name: string
  email: string
  phone: string
  position: string
  communicationPreference: 'EMAIL' | 'PHONE' | 'BOTH'
  createdBy: number
  createdAt: string
  updatedAt: string
}

interface ContactState {
  items: Contact[]
  selectedContact: Contact | null
  totalItems: number
  currentPage: number
  pageSize: number
  isLoading: boolean
  error: string | null
}

const initialState: ContactState = {
  items: [],
  selectedContact: null,
  totalItems: 0,
  currentPage: 0,
  pageSize: 20,
  isLoading: false,
  error: null
}

export const fetchContacts = createAsyncThunk(
  'contacts/fetchContacts',
  async ({ customerId, page = 0, size = 20 }: { customerId: number; page?: number; size?: number }) => {
    const response = await fetch(`/api/v1/customers/${customerId}/contacts?page=${page}&size=${size}`)
    if (!response.ok) {
      throw new Error('Failed to fetch contacts')
    }
    return response.json()
  }
)

export const createContact = createAsyncThunk(
  'contacts/createContact',
  async ({ customerId, contact }: { 
    customerId: number; 
    contact: Omit<Contact, 'id' | 'customerId' | 'createdBy' | 'createdAt' | 'updatedAt'>
  }) => {
    const response = await fetch(`/api/v1/customers/${customerId}/contacts`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(contact)
    })
    if (!response.ok) {
      throw new Error('Failed to create contact')
    }
    return response.json()
  }
)

export const updateContact = createAsyncThunk(
  'contacts/updateContact',
  async ({ customerId, id, contact }: { 
    customerId: number;
    id: number; 
    contact: Partial<Contact>
  }) => {
    const response = await fetch(`/api/v1/customers/${customerId}/contacts/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(contact)
    })
    if (!response.ok) {
      throw new Error('Failed to update contact')
    }
    return response.json()
  }
)

export const deleteContact = createAsyncThunk(
  'contacts/deleteContact',
  async ({ customerId, id }: { customerId: number; id: number }) => {
    const response = await fetch(`/api/v1/customers/${customerId}/contacts/${id}`, {
      method: 'DELETE'
    })
    if (!response.ok) {
      throw new Error('Failed to delete contact')
    }
    return id
  }
)

const contactSlice = createSlice({
  name: 'contacts',
  initialState,
  reducers: {
    setSelectedContact: (state, action: PayloadAction<Contact | null>) => {
      state.selectedContact = action.payload
    },
    setPageSize: (state, action: PayloadAction<number>) => {
      state.pageSize = action.payload
    },
    clearContacts: (state) => {
      state.items = []
      state.selectedContact = null
      state.totalItems = 0
      state.currentPage = 0
    }
  },
  extraReducers: (builder) => {
    builder
      // Fetch contacts
      .addCase(fetchContacts.pending, (state) => {
        state.isLoading = true
        state.error = null
      })
      .addCase(fetchContacts.fulfilled, (state, action) => {
        state.isLoading = false
        state.items = action.payload.content
        state.totalItems = action.payload.totalElements
        state.currentPage = action.payload.number
      })
      .addCase(fetchContacts.rejected, (state, action) => {
        state.isLoading = false
        state.error = action.error.message || 'Failed to fetch contacts'
      })
      // Create contact
      .addCase(createContact.fulfilled, (state, action) => {
        state.items.push(action.payload)
        state.totalItems += 1
      })
      // Update contact
      .addCase(updateContact.fulfilled, (state, action) => {
        const index = state.items.findIndex(item => item.id === action.payload.id)
        if (index !== -1) {
          state.items[index] = action.payload
        }
        if (state.selectedContact?.id === action.payload.id) {
          state.selectedContact = action.payload
        }
      })
      // Delete contact
      .addCase(deleteContact.fulfilled, (state, action) => {
        state.items = state.items.filter(item => item.id !== action.payload)
        state.totalItems -= 1
        if (state.selectedContact?.id === action.payload) {
          state.selectedContact = null
        }
      })
  }
})

export const { setSelectedContact, setPageSize, clearContacts } = contactSlice.actions
export default contactSlice.reducer