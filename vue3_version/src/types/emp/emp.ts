// 员工资料搜索参数
export interface EmpSelect {
  currentPage: number,
  size: number,
  politicId?: string,
  nationId?: string,
  posId?: string,
  jobLevelId?: string,
  departmentId?: string,
  beginDateScope?: string,
  engageForm?: string,
  name?: string
}
// 所有部门
export interface Deps {
  id?: number;
  name: string;
  parentId: number;
  depPath?: string;
  enabled?: boolean;
  isParent?: boolean;
  children?: Deps[];
  result?: any;
}
// 所有职称
export interface JobLevel {
  id?: number;
  name: string;
  titleLevel: string;
  createDate?: string;
  enabled?: boolean;
}
// 所有民族
export interface Nations {
  id: number;
  name: string;
}
// 所有政治面貌
export interface PoliticsStatus {
  id: number;
  name: string;
}
// 所有职位
export interface Position {
  id: number;
  name: string;
  createDate: string;
  enabled: boolean;
}

// 员工资料
export interface EmpState{
  id?: string,
  name: string,
  gender: string,
  birthday: string,
  idCard: string,
  wedlock: string,
  nationId: string,
  nativePlace: string,
  politicId: string,
  email: string,
  phone: string,
  address: string,
  departmentId: string,
  jobLevelId: string,
  posId: string,
  engageForm: string,
  tiptopDegree: string,
  specialty: string,
  school: string,
  beginDate: string,
  workState: string,
  workID: string,
  contractTerm: string,
  conversionTime: string,
  notworkDate: string,
  beginContract: string,
  endContract: string,
  workAge: string,
  salaryId?: string
}
