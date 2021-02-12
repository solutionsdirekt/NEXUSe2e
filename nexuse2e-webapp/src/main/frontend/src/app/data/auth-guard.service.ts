import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Injectable} from '@angular/core';
import {DataService} from "./data.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {
  constructor(private router: Router, private dataService: DataService) {
  }

  async canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
    try {
      await this.dataService.get('/loggedIn');
    } catch {
      await this.router.navigate(['/login'], { queryParams: { returnUrl: state.url, route: route }});
      return false;
    }
    return true;
  }
}
