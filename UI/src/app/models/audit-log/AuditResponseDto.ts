export interface AuditResponseDto{
    id:number;
    moduleName:string;
    url:string;
    httpMethod:string;
    entityName:string;
    objectId:number;
    methodName:string;
    ipAddress:string;
    timestamp:string;
    status:string;
    username:string;
    errorMessage:string;
}
