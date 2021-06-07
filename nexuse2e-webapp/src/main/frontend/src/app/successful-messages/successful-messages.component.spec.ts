import { ComponentFixture, TestBed } from "@angular/core/testing";

import { SuccessfulMessagesComponent } from "./successful-messages.component";
import { TranslateModule } from "@ngx-translate/core";
import { MatTabsModule } from "@angular/material/tabs";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { ListComponent } from "../list/list.component";
import { MatSnackBarModule } from "@angular/material/snack-bar";
import { MatDialogModule } from "@angular/material/dialog";
import { CapsToTitleCasePipe } from "../pipes/caps-to-title-case.pipe";
import { MatCardModule } from "@angular/material/card";
import { RouterTestingModule } from "@angular/router/testing";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { LoadingSpinnerComponent } from "../loading-spinner/loading-spinner.component";

describe("SuccessfulMessagesListComponent", () => {
  let component: SuccessfulMessagesComponent;
  let fixture: ComponentFixture<SuccessfulMessagesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        TranslateModule.forRoot(),
        MatTabsModule,
        BrowserAnimationsModule,
        MatSnackBarModule,
        MatDialogModule,
        MatCardModule,
        RouterTestingModule,
      ],
      declarations: [
        SuccessfulMessagesComponent,
        ListComponent,
        CapsToTitleCasePipe,
        LoadingSpinnerComponent,
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SuccessfulMessagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
