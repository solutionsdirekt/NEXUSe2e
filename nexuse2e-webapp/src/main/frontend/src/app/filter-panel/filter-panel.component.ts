import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { DateRange } from "../types";
import { ScreensizeService } from "../screensize.service";
import { SessionService } from "../data/session.service";

export enum FilterType {
  TEXT,
  SELECT,
  DATE_TIME_RANGE,
}

export interface Filter {
  fieldName: string;
  filterType: FilterType;
  allowedValues?: string[];
  defaultValue?: string | DateRange;
}

@Component({
  selector: "app-filter-panel",
  templateUrl: "./filter-panel.component.html",
  styleUrls: ["./filter-panel.component.scss"],
})
export class FilterPanelComponent implements OnInit {
  @Input() filters: Filter[] = [];
  @Input() itemType!: string;
  @Output() filterChange: EventEmitter<{
    [fieldName: string]: string | DateRange | undefined;
  }> = new EventEmitter();
  expanded = false;
  activeFilters: { [fieldName: string]: string | DateRange | undefined } = {};
  innerWidth = window.innerWidth;

  constructor(
    public screenSizeService: ScreensizeService,
    public sessionService: SessionService
  ) {}

  ngOnInit(): void {
    this.activeFilters = this.sessionService.getActiveFilters(this.itemType);
    if (Object.keys(this.activeFilters).length === 0) {
      this.setDefaultValues();
    }
    this.applyFilters();
  }

  getFilterType() {
    return FilterType;
  }

  toggleVisibility() {
    this.expanded = !this.expanded;
  }

  updateActiveFilter(filter: {
    fieldName: string;
    value?: string | DateRange;
  }) {
    if (filter.value) {
      this.activeFilters[filter.fieldName] = filter.value;
    } else {
      delete this.activeFilters[filter.fieldName];
    }
  }

  applyFilters() {
    this.filterChange.emit(this.activeFilters);
    this.sessionService.setActiveFilters(this.itemType, this.activeFilters);
  }

  handleFilterAction() {
    this.applyFilters();
    this.expanded = false;
  }

  getNumberOfActivatedFilters(): number {
    let activeLength = Object.keys(this.activeFilters).length;
    for (const value of Object.values(this.activeFilters)) {
      if (!(typeof value === "string")) {
        if (value?.startDate && value?.endDate) {
          activeLength++;
        }
      }
    }
    return activeLength;
  }

  resetFiltersAndSetDefaults() {
    this.activeFilters = {};
    this.setDefaultValues();
    this.applyFilters();
  }

  setDefaultValues() {
    for (const filter of this.filters) {
      if (filter.defaultValue && !this.activeFilters[filter.fieldName]) {
        this.activeFilters[filter.fieldName] = filter.defaultValue;
      }
    }
  }
}
