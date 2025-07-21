import { Injectable } from '@angular/core';
import { ethers } from 'ethers';

declare global {
    interface Window {
        ethereum: any;
    }
}

@Injectable({
    providedIn: 'root'
})
export class LoginService {
    private apiUrl = 'http://localhost:3000';
    private provider: any;

    constructor() {
        if (typeof window !== 'undefined' && window.ethereum) {
            this.provider = new ethers.BrowserProvider(window.ethereum);
        } else {
            alert('MetaMask not found. Please install it.');
        }
    }

    async connectWallet(): Promise<string | null> {
        try {
            if (!this.provider) {
                throw new Error('MetaMask is not installed');
            }

            const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' });
            return accounts[0];
        } catch (error) {
            console.error('Error connecting wallet:', error);
            return null;
        }
    }

    async requestSignature(message: string): Promise<string | null> {
        try {
            if (!this.provider) {
                throw new Error('MetaMask is not installed');
            }

            const signer = await this.provider.getSigner();
            const signature = await signer.signMessage(message);
            return signature;
        } catch (error) {
            console.error('Error requesting signature:', error);
            return null;
        }
    }
}
