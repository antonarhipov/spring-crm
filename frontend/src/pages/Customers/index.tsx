import { useState } from 'react'
import { Button, Card, Table } from '@/components/common'
import { Space } from 'antd'
import { EditOutlined, DeleteOutlined, PlusOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'

interface Customer {
  id: number
  name: string
  email: string
  phone: string
  status: 'active' | 'inactive'
}

const columns: ColumnsType<Customer> = [
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
    sorter: true,
  },
  {
    title: 'Email',
    dataIndex: 'email',
    key: 'email',
  },
  {
    title: 'Phone',
    dataIndex: 'phone',
    key: 'phone',
  },
  {
    title: 'Status',
    dataIndex: 'status',
    key: 'status',
    render: (status: string) => (
      <span style={{ 
        color: status === 'active' ? 'var(--success)' : 'var(--neutral-500)',
        textTransform: 'capitalize'
      }}>
        {status}
      </span>
    ),
  },
  {
    title: 'Actions',
    key: 'actions',
    render: (_: any, record: Customer) => (
      <Space size="middle">
        <Button variant="text" icon={<EditOutlined />}>
          Edit
        </Button>
        <Button variant="text" icon={<DeleteOutlined />} style={{ color: 'var(--error)' }}>
          Delete
        </Button>
      </Space>
    ),
  },
]

const mockData: Customer[] = [
  {
    id: 1,
    name: 'John Doe',
    email: 'john@example.com',
    phone: '(555) 123-4567',
    status: 'active',
  },
  {
    id: 2,
    name: 'Jane Smith',
    email: 'jane@example.com',
    phone: '(555) 987-6543',
    status: 'inactive',
  },
]

export const Customers = () => {
  const [loading] = useState(false)

  return (
    <div>
      <Card>
        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '24px' }}>
          <h1>Customers</h1>
          <Button icon={<PlusOutlined />}>Add Customer</Button>
        </div>
        <Table
          columns={columns}
          dataSource={mockData}
          loading={loading}
          rowKey="id"
        />
      </Card>
    </div>
  )
}

export default Customers