import { AuthorityResponse } from '../authority/AuthorityResponse';
import { ModuleResponse } from '../module/ModuleResponse';

export interface RoleResponse {
    id:number;
    libelle: string;
    actif:boolean;
    authoritys: AuthorityResponse[];
    module: ModuleResponse;
}
