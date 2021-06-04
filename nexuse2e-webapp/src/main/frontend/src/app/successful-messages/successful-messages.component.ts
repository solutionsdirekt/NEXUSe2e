import { Component, Input, OnInit } from "@angular/core";
import { Choreography, Partner } from "../types";
import {
  SUCCESS_MESS__CHOREOGRAPHY_CONFIG,
  SUCCESS_MESS__PARTNER_CONFIG,
} from "./successful-messages.config";
import { DataService } from "../services/data.service";

@Component({
  selector: "app-successful-messages-list",
  templateUrl: "./successful-messages.component.html",
  styleUrls: ["./successful-messages.component.scss"],
})
export class SuccessfulMessagesComponent implements OnInit {
  @Input() transactionActivityTimeframeInWeeks = 0;
  choreographies: Choreography[] | undefined;
  choreographyConfig = SUCCESS_MESS__CHOREOGRAPHY_CONFIG;
  partners: Partner[] | undefined;
  partnerConfig = SUCCESS_MESS__PARTNER_CONFIG;
  loaded = false;

  constructor(private dataService: DataService) {}

  async ngOnInit() {
    this.partners = await this.dataService.getStatisticsPartners();
    this.choreographies = await this.dataService.getStatisticsChoreographies();
    this.loaded = true;
  }
}