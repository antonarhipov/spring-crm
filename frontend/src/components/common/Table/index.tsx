import styled from 'styled-components'
import { Table as AntTable } from 'antd'
import type { TableProps } from 'antd'

const StyledTable = styled(AntTable)`
  .ant-table {
    background: white;
    border-radius: 8px;
    border: 1px solid var(--neutral-200);
  }

  .ant-table-thead > tr > th {
    background: var(--neutral-50);
    color: var(--neutral-900);
    font-weight: 600;
    border-bottom: 1px solid var(--neutral-200);
    padding: 16px 24px;
    
    &::before {
      display: none;
    }
  }

  .ant-table-tbody > tr > td {
    border-bottom: 1px solid var(--neutral-200);
    padding: 16px 24px;
    
    &:last-child {
      border-bottom: none;
    }
  }

  .ant-table-tbody > tr:hover > td {
    background: var(--neutral-50);
  }

  .ant-table-pagination {
    margin: 16px 24px;
  }

  .ant-table-column-sorter {
    color: var(--neutral-500);
  }

  .ant-table-column-sorter-up.active,
  .ant-table-column-sorter-down.active {
    color: var(--primary-500);
  }
`

export function Table<RecordType extends object = any>(
  props: TableProps<RecordType>
) {
  return <StyledTable<RecordType> {...props} />
}

export default Table