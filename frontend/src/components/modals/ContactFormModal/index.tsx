import React from 'react'
import { Modal } from 'antd'
import { useDispatch } from 'react-redux'
import { AppDispatch } from '@store/index'
import { createContact, updateContact } from '@store/slices/contactSlice'
import ContactForm from '@components/forms/ContactForm'

import { Contact, ContactFormData } from '@models'

interface ContactFormModalProps {
  visible: boolean
  customerId: number
  initialValues?: Partial<Contact>
  onClose: () => void
}

const ContactFormModal: React.FC<ContactFormModalProps> = ({
  visible,
  customerId,
  initialValues,
  onClose
}) => {
  const dispatch = useDispatch<AppDispatch>()
  const [isLoading, setIsLoading] = React.useState(false)

  const handleSubmit = async (values: ContactFormData) => {
    setIsLoading(true)
    try {
      if (initialValues?.id) {
        await dispatch(updateContact({
          customerId,
          id: initialValues.id,
          contact: values
        })).unwrap()
      } else {
        await dispatch(createContact({
          customerId,
          contact: values
        })).unwrap()
      }
      onClose()
    } catch (error) {
      console.error('Failed to save contact:', error)
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <Modal
      title={initialValues ? 'Edit Contact' : 'Create Contact'}
      open={visible}
      onCancel={onClose}
      footer={null}
      width={600}
      destroyOnClose
    >
      <ContactForm
        initialValues={initialValues}
        onSubmit={handleSubmit}
        onCancel={onClose}
        isLoading={isLoading}
      />
    </Modal>
  )
}

export default ContactFormModal
