import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { ClaimService } from '../../services/claim.service';
import { AccessibilityService } from '../../services/accessibility.service';
import { Claim } from '../../models/claim.model';

@Component({
  selector: 'app-my-claims',
  templateUrl: './my-claims.component.html',
  styleUrls: ['./my-claims.component.css']
})
export class MyClaimsComponent implements OnInit, OnDestroy {
  claims: Claim[] = [];
  filteredClaims: Claim[] = [];
  loading = true;
  error = '';
  
  filterStatus: string = 'ALL';
  searchTerm: string = '';
  sortBy: string = 'date-desc';

  private destroy$ = new Subject<void>();

  constructor(
    private claimService: ClaimService,
    private accessibilityService: AccessibilityService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.accessibilityService.announce('My claims page loaded', 'polite');
    this.loadClaims();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadClaims(): void {
    this.loading = true;
    
    this.claimService.getMyClaims()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (claims) => {
          this.claims = claims;
          this.applyFilters();
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to load claims';
          this.loading = false;
          this.accessibilityService.announce('Error loading claims', 'assertive');
        }
      });
  }

  applyFilters(): void {
    let result = [...this.claims];

    // Filter by status
    if (this.filterStatus !== 'ALL') {
      result = result.filter(claim => claim.status === this.filterStatus);
    }

    // Search
    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      result = result.filter(claim =>
        claim.description?.toLowerCase().includes(term) ||
        claim.location?.toLowerCase().includes(term) ||
        claim.disasterType?.toLowerCase().includes(term)
      );
    }

    // Sort
    result.sort((a, b) => {
      switch (this.sortBy) {
        case 'date-desc':
          return new Date(b.createdAt!).getTime() - new Date(a.createdAt!).getTime();
        case 'date-asc':
          return new Date(a.createdAt!).getTime() - new Date(b.createdAt!).getTime();
        case 'amount-desc':
          return (b.requestAmount || 0) - (a.requestAmount || 0);
        case 'amount-asc':
          return (a.requestAmount || 0) - (b.requestAmount || 0);
        default:
          return 0;
      }
    });

    this.filteredClaims = result;
  }

  onFilterChange(): void {
    this.applyFilters();
    this.accessibilityService.announce(`Showing ${this.filteredClaims.length} claims`, 'polite');
  }

  getStatusClass(status?: string): string {
    switch (status) {
      case 'PENDING': return 'status-pending';
      case 'APPROVED': return 'status-approved';
      case 'REJECTED': return 'status-rejected';
      case 'UNDER_REVIEW': return 'status-review';
      default: return 'status-default';
    }
  }

  getDisasterTypeLabel(type?: string): string {
    const labels: { [key: string]: string } = {
      'FLOOD': 'Flood',
      'HURRICANE': 'Hurricane',
      'WILDFIRE': 'Wildfire',
      'TORNADO': 'Tornado',
      'EARTHQUAKE': 'Earthquake',
      'OTHER': 'Other'
    };
    return labels[type || 'OTHER'] || 'Unknown';
  }

  navigateToNewClaim(): void {
    this.router.navigate(['/claim-form']);
  }

  navigateToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }
}
