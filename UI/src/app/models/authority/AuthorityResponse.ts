import { ModuleResponse } from '../module/ModuleResponse';
import { AuthorityTypeResponse } from '../authorityType/AuthorityTypeResponse';

export interface AuthorityResponse {
    id: number;
    libelle: string;
    actif: boolean;
    module: ModuleResponse;
    authorityType: AuthorityTypeResponse;
}
