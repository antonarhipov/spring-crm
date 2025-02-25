import React, { useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import { Card, Tabs, Button, Descriptions, Tag, Space, Table, Typography, Modal, message } from 'antd'
import ContactFormModal from '@components/modals/ContactFormModal'
import { EditOutlined, DeleteOutlined, PlusOutlined } from '@ant-design/icons'
import styled from 'styled-components'
import { RootState, AppDispatch } from '@store/index'
import { fetchContacts, deleteContact } from '@store/slices/contactSlice'
import { deleteCustomer } from '@store/slices/customerSlice'
import type { Contact } from '@models'

const { Title } = Typography
const { TabPane } = Tabs

const PageHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
`

const ActionButtons = styled.div`
  display: flex;
  gap: 16px;
`


const statusColors = {
  ACTIVE: 'success',
  INACTIVE: 'default',
  LEAD: 'warning'
}

const categoryColors = {
  REGULAR: 'blue',
  VIP: 'purple',
  PREMIUM: 'gold'
}

const CustomerDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const dispatch = useDispatch<AppDispatch>()
  const customer = useSelector((state: RootState) => 
    state.customers.items.find(c => c.id === Number(id))
  )
  const { items: contacts, isLoading } = useSelector((state: RootState) => state.contacts)

  useEffect(() => {
    if (id) {
      dispatch(fetchContacts({ customerId: Number(id) }))
    }
  }, [dispatch, id])

  const contactColumns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name'
    },
    {
      title: 'Position',
      dataIndex: 'position',
      key: 'position'
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email'
    },
    {
      title: 'Phone',
      dataIndex: 'phone',
      key: 'phone'
    },
    {
      title: 'Preference',
      dataIndex: 'communicationPreference',
      key: 'communicationPreference',
      render: (pref: string) => <Tag>{pref}</Tag>
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_: unknown, record: Contact) => (
        <Space>
          <Button
            type="text"
            icon={<EditOutlined />}
            onClick={() => handleEditContact(record)}
          />
          <Button
            type="text"
            danger
            icon={<DeleteOutlined />}
            onClick={() => handleDeleteContact(record.id)}
          />
        </Space>
      )
    }
  ]

  const handleEditCustomer = () => {
    // TODO: Implement customer edit
  }

  const handleDeleteCustomer = () => {
    Modal.confirm({
      title: 'Delete Customer',
      content: `Are you sure you want to delete customer "${customer?.name}"? This will also delete all associated contacts.`,
      okText: 'Yes',
      okType: 'danger',
      cancelText: 'No',
      onOk: async () => {
        try {
          await dispatch(deleteCustomer(Number(id))).unwrap()
          message.success('Customer deleted successfully')
          navigate('/customers')
        } catch (error) {
          message.error('Failed to delete customer')
        }
      }
    })
  }

  const [isContactModalVisible, setIsContactModalVisible] = React.useState(false)
  const [selectedContact, setSelectedContact] = React.useState<Partial<Contact> | undefined>(undefined)

  const handleAddContact = () => {
    setSelectedContact(undefined)
    setIsContactModalVisible(true)
  }

  const handleEditContact = (contact: Contact) => {
    setSelectedContact(contact)
    setIsContactModalVisible(true)
  }

  const handleContactModalClose = () => {
    setIsContactModalVisible(false)
    setSelectedContact(undefined)
  }

  const handleDeleteContact = (contactId: number) => {
    Modal.confirm({
      title: 'Delete Contact',
      content: 'Are you sure you want to delete this contact?',
      okText: 'Yes',
      okType: 'danger',
      cancelText: 'No',
      onOk: async () => {
        try {
          await dispatch(deleteContact({ customerId: Number(id), id: contactId })).unwrap()
          message.success('Contact deleted successfully')
        } catch (error) {
          message.error('Failed to delete contact')
        }
      }
    })
  }

  if (!customer) {
    return <div>Customer not found</div>
  }

  return (
    <div>
      <ContactFormModal
        visible={isContactModalVisible}
        customerId={Number(id)}
        initialValues={selectedContact}
        onClose={handleContactModalClose}
      />
      <PageHeader>
        <Title level={2}>{customer.name}</Title>
        <ActionButtons>
          <Button
            type="primary"
            icon={<EditOutlined />}
            onClick={handleEditCustomer}
          >
            Edit Customer
          </Button>
          <Button
            danger
            icon={<DeleteOutlined />}
            onClick={handleDeleteCustomer}
          >
            Delete Customer
          </Button>
        </ActionButtons>
      </PageHeader>

      <Tabs defaultActiveKey="details">
        <TabPane tab="Details" key="details">
          <Card>
            <Descriptions bordered column={2}>
              <Descriptions.Item label="Email">{customer.email}</Descriptions.Item>
              <Descriptions.Item label="Phone">{customer.phone}</Descriptions.Item>
              <Descriptions.Item label="Status">
                <Tag color={statusColors[customer.status]}>{customer.status}</Tag>
              </Descriptions.Item>
              <Descriptions.Item label="Category">
                <Tag color={categoryColors[customer.category]}>{customer.category}</Tag>
              </Descriptions.Item>
              <Descriptions.Item label="Created At">
                {new Date(customer.createdAt).toLocaleDateString()}
              </Descriptions.Item>
              <Descriptions.Item label="Updated At">
                {new Date(customer.updatedAt).toLocaleDateString()}
              </Descriptions.Item>
            </Descriptions>
          </Card>
        </TabPane>

        <TabPane tab="Contacts" key="contacts">
          <Card
            title="Contacts"
            extra={
              <Button
                type="primary"
                icon={<PlusOutlined />}
                onClick={handleAddContact}
              >
                Add Contact
              </Button>
            }
          >
            <Table
              columns={contactColumns}
              dataSource={contacts as Contact[]}
              rowKey="id"
              loading={isLoading}
            />
          </Card>
        </TabPane>
      </Tabs>
    </div>
  )
}

export default CustomerDetails
