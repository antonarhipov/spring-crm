import React, { useEffect } from 'react'
import { Table, Card, Button, Tag, Space, Typography, Modal, message } from 'antd'
import UserFormModal from '@components/modals/UserFormModal'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons'
import { useDispatch, useSelector } from 'react-redux'
import styled from 'styled-components'
import { RootState, AppDispatch } from '@store/index'
import { fetchUsers, deleteUser } from '@store/slices/userSlice'

const { Title } = Typography
const { confirm } = Modal

const PageHeader = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
`

import { User, UserRole } from '@models'

const roleColors: Record<UserRole, string> = {
  ADMIN: 'red',
  SALES_MANAGER: 'blue',
  SALES_REPRESENTATIVE: 'green',
  READ_ONLY: 'default'
}

const Users: React.FC = () => {
  const dispatch = useDispatch<AppDispatch>()
  const { items: users, isLoading, totalItems } = useSelector((state: RootState) => state.users)
  const [currentPage, setCurrentPage] = React.useState(1)
  const pageSize = 10

  useEffect(() => {
    dispatch(fetchUsers({ page: currentPage - 1, size: pageSize }))
  }, [dispatch, currentPage])

  const columns = [
    {
      title: 'Username',
      dataIndex: 'username',
      key: 'username'
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email'
    },
    {
      title: 'Roles',
      dataIndex: 'roles',
      key: 'roles',
      render: (roles: UserRole[]) => (
        <Space size={[0, 4]} wrap>
          {roles.map(role => (
            <Tag color={roleColors[role]} key={role}>
              {role.replace('_', ' ')}
            </Tag>
          ))}
        </Space>
      )
    },
    {
      title: 'Created At',
      dataIndex: 'createdAt',
      key: 'createdAt',
      render: (date: string) => new Date(date).toLocaleDateString()
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_: unknown, record: User) => (
        <Space>
          <Button
            type="text"
            icon={<EditOutlined />}
            onClick={() => handleEditUser(record)}
          />
          <Button
            type="text"
            danger
            icon={<DeleteOutlined />}
            onClick={() => handleDeleteUser(record)}
          />
        </Space>
      )
    }
  ]

  const [isModalVisible, setIsModalVisible] = React.useState(false)
  const [selectedUser, setSelectedUser] = React.useState<Partial<User> | undefined>(undefined)

  const handleAddUser = () => {
    setSelectedUser(undefined)
    setIsModalVisible(true)
  }

  const handleEditUser = (user: User) => {
    setSelectedUser(user)
    setIsModalVisible(true)
  }

  const handleModalClose = () => {
    setIsModalVisible(false)
    setSelectedUser(undefined)
  }

  const handleDeleteUser = (user: User) => {
    confirm({
      title: 'Are you sure you want to delete this user?',
      content: `This will permanently delete the user "${user.username}".`,
      okText: 'Yes',
      okType: 'danger',
      cancelText: 'No',
      onOk: async () => {
        try {
          await dispatch(deleteUser(user.id)).unwrap()
          message.success('User deleted successfully')
        } catch (error) {
          message.error('Failed to delete user')
        }
      }
    })
  }

  return (
    <div>
      <UserFormModal
        visible={isModalVisible}
        initialValues={selectedUser}
        onClose={handleModalClose}
      />
      <PageHeader>
        <Title level={2}>Users</Title>
        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={handleAddUser}
        >
          Add User
        </Button>
      </PageHeader>

      <Card>
        <Table
          columns={columns}
          dataSource={users}
          rowKey="id"
          loading={isLoading}
          pagination={{
            current: currentPage,
            pageSize,
            total: totalItems,
            onChange: setCurrentPage
          }}
        />
      </Card>
    </div>
  )
}

export default Users
