import { Component, OnInit } from "@angular/core";
import { DateRange, Message } from "../types";
import { DataService } from "../data/data.service";
import {
  Filter, FilterType,
} from "../filter-panel/filter-panel.component";
import { ListConfig } from "../list/list.component";

@Component({
  selector: "app-message-list",
  templateUrl: "./message-list.component.html",
  styleUrls: ["./message-list.component.scss"],
})
export class MessageListComponent implements OnInit {
  totalMessageCount?: number;
  messages: Message[] = [];
  loaded = false;
  static readonly START_DATE_DEFAULT: Date = new Date(
    new Date().setHours(0, 0, 0, 0)
  );
  static readonly END_DATE_DEFAULT: Date = new Date(
    new Date().setHours(24, 0, 0, 0)
  );

  private participantFilter: Filter = {
    fieldName: "partnerId",
    filterType: FilterType.TEXT,
  };
  private choreographyFilter: Filter = {
    fieldName: "choreographyId",
    filterType: FilterType.TEXT,
  };

  filters = [
    {
      fieldName: "startEndDateRange",
      filterType: FilterType.DATE_TIME_RANGE,
      defaultValue: {
        startDate: MessageListComponent.START_DATE_DEFAULT,
        endDate: MessageListComponent.END_DATE_DEFAULT,
      },
    },
    {
      fieldName: "messageId",
      filterType: FilterType.TEXT,
    },
    {
      fieldName: "conversationId",
      filterType: FilterType.TEXT,
    },
    this.choreographyFilter,
    this.participantFilter,
    {
      fieldName: "status",
      filterType: FilterType.SELECT,
      allowedValues: [
        "FAILED",
        "SENT",
        "UNKNOWN",
        "RETRYING",
        "QUEUED",
        "STOPPED",
      ],
    },
    {
      fieldName: "type",
      filterType: FilterType.SELECT,
      allowedValues: ["NORMAL", "ACKNOWLEDGEMENT", "ERROR"],
      defaultValue: "NORMAL",
    },
  ];

  activeFilters: { [fieldName: string]: string | DateRange | undefined } = {};

  mobileConfig: ListConfig[] = [
    {
      fieldName: "messageId",
      linkUrlRecipe: "$nxMessageId$",
      isHeader: true,
    },
    {
      fieldName: "conversationId",
      linkUrlRecipe: "../conversations/$nxConversationId$",
    },
    { fieldName: "partnerId" },
    { fieldName: "typeName" },
    { fieldName: "actionId" },
    { fieldName: "createdDate" },
  ];

  desktopConfig: ListConfig[] = [
    {
      fieldName: "messageId",
      linkUrlRecipe: "$nxMessageId$",
    },
    {
      fieldName: "conversationId",
      linkUrlRecipe: "../conversations/$nxConversationId$",
    },
    { fieldName: "partnerId" },
    { fieldName: "status" },
    { fieldName: "backendStatus" },
    { fieldName: "typeName" },
    { fieldName: "choreographyId" },
    { fieldName: "actionId" },
    { fieldName: "createdDate" },
    { fieldName: "turnAroundTime" },
  ];

  constructor(private dataService: DataService) {}

  async ngOnInit() {
    this.participantFilter.allowedValues = await this.dataService.getPartnerIds();
    this.choreographyFilter.allowedValues = await this.dataService.getChoreographyIds();
  }

  async loadMessages(pageIndex: number, pageSize: number) {
    this.loaded = false;
    this.messages = await this.dataService.getMessages(
      pageIndex,
      pageSize,
      this.activeFilters
    );
    this.loaded = true;
  }

  async refreshMessageCount() {
    this.totalMessageCount = await this.dataService.getMessagesCount(
      this.activeFilters
    );
  }

  filterMessages(activeFilters: { [fieldName: string]: string | DateRange | undefined }) {
    this.activeFilters = activeFilters;
    this.refreshMessageCount();
  }
}
