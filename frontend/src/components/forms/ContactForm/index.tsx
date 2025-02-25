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

import { ContactFormData, CommunicationPreference } from '@models'

interface ContactFormProps {
  initialValues?: Partial<ContactFormData>
  onSubmit: (values: ContactFormData) => void
  onCancel: () => void
  isLoading?: boolean
}

const ContactForm: React.FC<ContactFormProps> = ({
  initialValues,
  onSubmit,
  onCancel,
  isLoading = false
}) => {
  const [form] = Form.useForm<ContactFormData>()

  const handleSubmit = async (values: ContactFormData) => {
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
          { required: true, message: 'Please enter contact name' },
          { min: 2, message: 'Name must be at least 2 characters' }
        ]}
      >
        <Input placeholder="Enter contact name" />
      </Form.Item>

      <Form.Item
        label="Position"
        name="position"
        rules={[{ required: true, message: 'Please enter contact position' }]}
      >
        <Input placeholder="Enter contact position" />
      </Form.Item>

      <Form.Item
        label="Email"
        name="email"
        rules={[
          { required: true, message: 'Please enter contact email' },
          { type: 'email', message: 'Please enter a valid email' }
        ]}
      >
        <Input placeholder="Enter contact email" />
      </Form.Item>

      <Form.Item
        label="Phone"
        name="phone"
        rules={[
          { required: true, message: 'Please enter contact phone' },
          { pattern: /^\+?[\d\s-()]+$/, message: 'Please enter a valid phone number' }
        ]}
      >
        <Input placeholder="Enter contact phone" />
      </Form.Item>

      <Form.Item
        label="Communication Preference"
        name="communicationPreference"
        rules={[{ required: true, message: 'Please select communication preference' }]}
      >
        <Select placeholder="Select communication preference">
          <Option value={CommunicationPreference.EMAIL}>Email</Option>
          <Option value={CommunicationPreference.PHONE}>Phone</Option>
          <Option value={CommunicationPreference.BOTH}>Both</Option>
        </Select>
      </Form.Item>

      <FormActions>
        <Button onClick={onCancel}>
          Cancel
        </Button>
        <Button type="primary" htmlType="submit" loading={isLoading}>
          {initialValues ? 'Update Contact' : 'Create Contact'}
        </Button>
      </FormActions>
    </Form>
  )
}

export default ContactForm
