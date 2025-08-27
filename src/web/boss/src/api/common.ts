// 分页

/**
 * 分页
 */
export interface Pager {
  page: number // 当前页
  pageSize: number // 分页大小
}

/**
 * 分页结果
 */
export interface Pagination<T> {
  page: number // 当前页
  pageSize: number // 分页大小
  count: number // 当前页返回数量
  total: number // 查询总数
  totalPage: number // 总页数
  prevPage: number // 上一页
  nextPage: number // 下一页
  items: T[] // 数据列表
}

// 级联数据定义
export interface CascaderOption {
  value: string
  label: string
  children?: CascaderOption[]
}

export interface ValueObject {
  code: string
  name: string
}