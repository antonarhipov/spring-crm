import React from 'react'
import { Form, Input, Select, Button } from 'antd'
import styled from 'styled-components'

const { Option } = Select

const FormActions = styled.div`
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 24px;
`

import { CustomerFormData, CustomerStatus, CustomerCategory } from '@models'

interface CustomerFormProps {
  initialValues?: Partial<CustomerFormData>
  onSubmit: (values: CustomerFormData) => void
  onCancel: () => void
  isLoading?: boolean
}

const CustomerForm: React.FC<CustomerFormProps> = ({
  initialValues,
  onSubmit,
  onCancel,
  isLoading = false
}) => {
  const [form] = Form.useForm<CustomerFormData>()

  const handleSubmit = async (values: CustomerFormData) => {
    try {
      await form.validateFields()
      onSubmit(values)
    } catch (error) {
      console.error('Validation failed:', error)
    }
  }

  return (
    <Form
      form={form}
      layout="vertical"
      initialValues={initialValues}
      onFinish={handleSubmit}
    >
      <Form.Item
        label="Name"
        name="name"
        rules={[
          { required: true, message: 'Please enter customer name' },
          { min: 2, message: 'Name must be at least 2 characters' }
        ]}
      >
        <Input placeholder="Enter customer name" />
      </Form.Item>

      <Form.Item
        label="Email"
        name="email"
        rules={[
          { required: true, message: 'Please enter customer email' },
          { type: 'email', message: 'Please enter a valid email' }
        ]}
      >
        <Input placeholder="Enter customer email" />
      </Form.Item>

      <Form.Item
        label="Phone"
        name="phone"
        rules={[
          { required: true, message: 'Please enter customer phone' },
          { pattern: /^\+?[\d\s-()]+$/, message: 'Please enter a valid phone number' }
        ]}
      >
        <Input placeholder="Enter customer phone" />
      </Form.Item>

      <Form.Item
        label="Status"
        name="status"
        rules={[{ required: true, message: 'Please select customer status' }]}
      >
        <Select placeholder="Select customer status">
          <Option value={CustomerStatus.ACTIVE}>Active</Option>
          <Option value={CustomerStatus.INACTIVE}>Inactive</Option>
          <Option value={CustomerStatus.LEAD}>Lead</Option>
        </Select>
      </Form.Item>

      <Form.Item
        label="Category"
        name="category"
        rules={[{ required: true, message: 'Please select customer category' }]}
      >
        <Select placeholder="Select customer category">
          <Option value={CustomerCategory.REGULAR}>Regular</Option>
          <Option value={CustomerCategory.VIP}>VIP</Option>
          <Option value={CustomerCategory.PREMIUM}>Premium</Option>
        </Select>
      </Form.Item>

      <FormActions>
        <Button onClick={onCancel}>
          Cancel
        </Button>
        <Button type="primary" htmlType="submit" loading={isLoading}>
          {initialValues ? 'Update Customer' : 'Create Customer'}
        </Button>
      </FormActions>
    </Form>
  )
}

export default CustomerForm
