import { ProfileResponse } from '../profile/ProfileResponse';

export interface Users {
    id: number;
    firstName: string;
    lastName: string;
    userName: string;
    email: string;
    uuid: string;
    phoneNumber: string;
    actif: boolean;
    profiles: ProfileResponse[];
    actifProfile: ProfileResponse;
}
