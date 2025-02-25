import React, { useEffect, useState } from 'react'
import { Table, Card, Input, Button, Space, Tag, Select, Typography } from 'antd'
import CustomerFormModal from '@components/modals/CustomerFormModal'
import { PlusOutlined, SearchOutlined, EditOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import { useDispatch, useSelector } from 'react-redux'
import styled from 'styled-components'
import { RootState, AppDispatch } from '@store/index'
import { fetchCustomers } from '@store/slices/customerSlice'
import { Customer, CustomerStatus, CustomerCategory } from '@models'

const { Title } = Typography
const { Option } = Select

const PageHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
`

const SearchContainer = styled.div`
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
`


const statusColors: Record<CustomerStatus, string> = {
  [CustomerStatus.ACTIVE]: 'success',
  [CustomerStatus.INACTIVE]: 'default',
  [CustomerStatus.LEAD]: 'warning'
}

const categoryColors: Record<CustomerCategory, string> = {
  [CustomerCategory.REGULAR]: 'blue',
  [CustomerCategory.VIP]: 'purple',
  [CustomerCategory.PREMIUM]: 'gold'
}

const Customers: React.FC = () => {
  const navigate = useNavigate()
  const dispatch = useDispatch<AppDispatch>()
  const { items: customers, isLoading, totalItems } = useSelector((state: RootState) => state.customers)
  const [searchText, setSearchText] = useState('')
  const [statusFilter, setStatusFilter] = useState<string | undefined>()
  const [categoryFilter, setCategoryFilter] = useState<string | undefined>()
  const [currentPage, setCurrentPage] = useState(1)
  const [pageSize, setPageSize] = useState(20)

  const handleTableChange = (pagination: any) => {
    const { current, pageSize: newPageSize } = pagination
    setCurrentPage(current)
    setPageSize(newPageSize)
  }


  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      render: (text: string, record: Customer) => (
        <Space>
          <a onClick={() => navigate(`/customers/${record.id}`)}>{text}</a>
          <Button
            type="text"
            icon={<EditOutlined />}
            onClick={(e) => {
              e.stopPropagation()
              handleEditCustomer(record)
            }}
          />
        </Space>
      )
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
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (status: CustomerStatus) => (
        <Tag color={statusColors[status]}>{status}</Tag>
      )
    },
    {
      title: 'Category',
      dataIndex: 'category',
      key: 'category',
      render: (category: CustomerCategory) => (
        <Tag color={categoryColors[category]}>{category}</Tag>
      )
    },
    {
      title: 'Created At',
      dataIndex: 'createdAt',
      key: 'createdAt',
      render: (date: string) => new Date(date).toLocaleDateString()
    }
  ]

  useEffect(() => {
    const params = {
      page: currentPage - 1,
      size: pageSize,
      ...(searchText && { query: searchText }),
      ...(statusFilter && { status: statusFilter }),
      ...(categoryFilter && { category: categoryFilter })
    }
    dispatch(fetchCustomers(params))
  }, [dispatch, currentPage, pageSize, searchText, statusFilter, categoryFilter])

  const handleSearch = () => {
    setCurrentPage(1)
  }

  const handleStatusChange = (value: string | undefined) => {
    setStatusFilter(value)
    setCurrentPage(1)
  }

  const handleCategoryChange = (value: string | undefined) => {
    setCategoryFilter(value)
    setCurrentPage(1)
  }

  const [isModalVisible, setIsModalVisible] = React.useState(false)
  const [selectedCustomer, setSelectedCustomer] = React.useState<Partial<Customer> | undefined>(undefined)

  const handleCreateCustomer = () => {
    setSelectedCustomer(undefined)
    setIsModalVisible(true)
  }

  const handleEditCustomer = (customer: Customer) => {
    setSelectedCustomer(customer)
    setIsModalVisible(true)
  }

  const handleModalClose = () => {
    setIsModalVisible(false)
    setSelectedCustomer(undefined)
  }

  return (
    <div>
      <CustomerFormModal
        visible={isModalVisible}
        initialValues={selectedCustomer}
        onClose={handleModalClose}
      />
      <PageHeader>
        <Title level={2}>Customers</Title>
        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={handleCreateCustomer}
        >
          Add Customer
        </Button>
      </PageHeader>

      <Card>
        <SearchContainer>
          <Input
            placeholder="Search customers..."
            prefix={<SearchOutlined />}
            value={searchText}
            onChange={(e) => setSearchText(e.target.value)}
            onPressEnter={handleSearch}
            style={{ width: 300 }}
          />
          <Select
            placeholder="Status"
            allowClear
            style={{ width: 120 }}
            value={statusFilter}
            onChange={handleStatusChange}
          >
            <Option value="ACTIVE">Active</Option>
            <Option value="INACTIVE">Inactive</Option>
            <Option value="LEAD">Lead</Option>
          </Select>
          <Select
            placeholder="Category"
            allowClear
            style={{ width: 120 }}
            value={categoryFilter}
            onChange={handleCategoryChange}
          >
            <Option value="REGULAR">Regular</Option>
            <Option value="VIP">VIP</Option>
            <Option value="PREMIUM">Premium</Option>
          </Select>
        </SearchContainer>

        <Table
          columns={columns}
          dataSource={customers as Customer[]}
          rowKey="id"
          loading={isLoading}
          pagination={{
            current: currentPage,
            pageSize,
            total: totalItems,
            pageSizeOptions: ['10', '20', '50', '100'],
            showSizeChanger: true,
            showTotal: (total) => `Total ${total} customers`
          }}
          onChange={handleTableChange}
        />
      </Card>
    </div>
  )
}

export default Customers
