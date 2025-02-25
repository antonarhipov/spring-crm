import React from 'react'
import { Modal } from 'antd'
import { useDispatch } from 'react-redux'
import { AppDispatch } from '@store/index'
import { createCustomer, updateCustomer } from '@store/slices/customerSlice'
import CustomerForm from '@components/forms/CustomerForm'

import { Customer, CustomerFormData } from '@models'

interface CustomerFormModalProps {
  visible: boolean
  initialValues?: Partial<Customer>
  onClose: () => void
}

const CustomerFormModal: React.FC<CustomerFormModalProps> = ({
  visible,
  initialValues,
  onClose
}) => {
  const dispatch = useDispatch<AppDispatch>()
  const [isLoading, setIsLoading] = React.useState(false)

  const handleSubmit = async (values: CustomerFormData) => {
    setIsLoading(true)
    try {
      if (initialValues?.id) {
        await dispatch(updateCustomer({ id: initialValues.id, customer: values })).unwrap()
      } else {
        await dispatch(createCustomer(values)).unwrap()
      }
      onClose()
    } catch (error) {
      console.error('Failed to save customer:', error)
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <Modal
      title={initialValues ? 'Edit Customer' : 'Create Customer'}
      open={visible}
      onCancel={onClose}
      footer={null}
      width={600}
      destroyOnClose
    >
      <CustomerForm
        initialValues={initialValues}
        onSubmit={handleSubmit}
        onCancel={onClose}
        isLoading={isLoading}
      />
    </Modal>
  )
}

export default CustomerFormModal
