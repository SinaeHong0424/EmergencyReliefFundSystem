import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ClaimService } from '../../services/claim.service';
import { AuthService } from '../../services/auth.service';
import { Claim } from '../../models/claim.model';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  currentUser: any = null;
  allClaims: Claim[] = [];
  pendingClaims: Claim[] = [];
  selectedClaim: Claim | null = null;
  loading = true;
  error = '';

  stats = {
    total: 0,
    pending: 0,
    underReview: 0,
    approved: 0,
    rejected: 0,
    paid: 0
  };

  reviewComments = '';
  approvedAmount: number | undefined = undefined;

  constructor(
    private claimService: ClaimService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    
    if (!this.currentUser || this.currentUser.role !== 'ROLE_ADMIN') {
      this.router.navigate(['/dashboard']);
      return;
    }

    this.loadAdminData();
  }

  loadAdminData(): void {
    this.loading = true;

    this.claimService.getStatistics().subscribe({
      next: (stats) => {
        this.stats = stats as any;
      },
      error: (error) => console.error('Failed to load statistics:', error)
    });

    this.claimService.getAllClaims().subscribe({
      next: (claims) => {
        this.allClaims = claims;
        this.pendingClaims = claims.filter(c => c.status === 'PENDING');
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load claims:', error);
        this.error = 'Failed to load claims';
        this.loading = false;
      }
    });
  }

  selectClaim(claim: Claim): void {
    this.selectedClaim = claim;
    this.reviewComments = '';
    this.approvedAmount = claim.requestAmount ? Number(claim.requestAmount) : undefined;
  }

  approveClaim(): void {
    if (!this.selectedClaim) return;

    const approvalData: { reviewComments: string; approvedAmount?: number } = {
      reviewComments: this.reviewComments
    };

    if (this.approvedAmount !== undefined) {
      approvalData.approvedAmount = this.approvedAmount;
    }

    this.claimService.approveClaim(this.selectedClaim.id!, approvalData).subscribe({
      next: (updatedClaim) => {
        alert('Claim approved successfully!');
        this.selectedClaim = null;
        this.loadAdminData();
      },
      error: (error) => {
        console.error('Failed to approve claim:', error);
        alert('Failed to approve claim');
      }
    });
  }

  rejectClaim(): void {
    if (!this.selectedClaim) return;

    if (!this.reviewComments.trim()) {
      alert('Please provide rejection reason');
      return;
    }

    const rejectionData = {
      reviewComments: this.reviewComments
    };

    this.claimService.rejectClaim(this.selectedClaim.id!, rejectionData).subscribe({
      next: (updatedClaim) => {
        alert('Claim rejected');
        this.selectedClaim = null;
        this.loadAdminData();
      },
      error: (error) => {
        console.error('Failed to reject claim:', error);
        alert('Failed to reject claim');
      }
    });
  }

  cancelReview(): void {
    this.selectedClaim = null;
    this.reviewComments = '';
    this.approvedAmount = undefined;
  }

  getStatusClass(status?: string): string {
    switch (status) {
      case 'PENDING': return 'status-pending';
      case 'UNDER_REVIEW': return 'status-review';
      case 'APPROVED': return 'status-approved';
      case 'REJECTED': return 'status-rejected';
      case 'PAID': return 'status-paid';
      default: return 'status-default';
    }
  }

  formatCurrency(amount?: number): string {
    return amount ? `$${amount.toLocaleString()}` : '$0';
  }

  formatDate(date?: string): string {
    return date ? new Date(date).toLocaleDateString() : 'N/A';
  }

  logout(): void {
    this.authService.logout();
  }
}
