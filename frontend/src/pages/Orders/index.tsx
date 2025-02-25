import { useState } from 'react'
import { Button, Card, Table } from '@/components/common'
import { Space, Tag } from 'antd'
import { EyeOutlined, PlusOutlined } from '@ant-design/icons'
import type { ColumnsType } from 'antd/es/table'

interface Order {
  id: number
  orderNumber: string
  customer: string
  date: string
  total: number
  status: 'pending' | 'processing' | 'completed' | 'cancelled'
}

const getStatusColor = (status: Order['status']) => {
  switch (status) {
    case 'pending':
      return 'var(--warning)'
    case 'processing':
      return 'var(--info)'
    case 'completed':
      return 'var(--success)'
    case 'cancelled':
      return 'var(--error)'
    default:
      return 'var(--neutral-500)'
  }
}

const columns: ColumnsType<Order> = [
  {
    title: 'Order Number',
    dataIndex: 'orderNumber',
    key: 'orderNumber',
    sorter: true,
  },
  {
    title: 'Customer',
    dataIndex: 'customer',
    key: 'customer',
  },
  {
    title: 'Date',
    dataIndex: 'date',
    key: 'date',
    sorter: true,
  },
  {
    title: 'Total',
    dataIndex: 'total',
    key: 'total',
    render: (total: number) => `$${total.toFixed(2)}`,
    sorter: true,
  },
  {
    title: 'Status',
    dataIndex: 'status',
    key: 'status',
    render: (status: Order['status']) => (
      <Tag color={getStatusColor(status)} style={{ textTransform: 'capitalize' }}>
        {status}
      </Tag>
    ),
  },
  {
    title: 'Actions',
    key: 'actions',
    render: (_: any, record: Order) => (
      <Space size="middle">
        <Button variant="text" icon={<EyeOutlined />}>
          View Details
        </Button>
      </Space>
    ),
  },
]

const mockData: Order[] = [
  {
    id: 1,
    orderNumber: 'ORD-2024-001',
    customer: 'John Doe',
    date: '2024-02-25',
    total: 299.99,
    status: 'completed',
  },
  {
    id: 2,
    orderNumber: 'ORD-2024-002',
    customer: 'Jane Smith',
    date: '2024-02-25',
    total: 149.50,
    status: 'processing',
  },
]

export const Orders = () => {
  const [loading] = useState(false)

  return (
    <div>
      <Card>
        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '24px' }}>
          <h1>Orders</h1>
          <Button icon={<PlusOutlined />}>New Order</Button>
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

export default Orders