// 奖惩信息参数
export interface Jl {
  eid: string,
  ecReason: string,
  ecType: string,
  ecPoint: string,
}
// 删除勾选中的值
export interface MultipleSelection {
  eid: string,
  ecReason: string,
  ecType: string,
  ecPoint: string,
  ecDate: string,
  id: string
}
export interface UpdateJl {
  ecReason: string,
  ecPoint: string,
  createDate:string,
  ecDate: string,
	ecType: string,
	eid: string,
	id: string,
	remark:string
}