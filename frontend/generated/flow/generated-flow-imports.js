import { injectGlobalCss } from 'Frontend/generated/jar-resources/theme-util.js';

import { css, unsafeCSS, registerStyles } from '@vaadin/vaadin-themable-mixin';
import $cssFromFile_0 from 'Frontend/views.css?inline';
const $css_0 = typeof $cssFromFile_0  === 'string' ? unsafeCSS($cssFromFile_0) : $cssFromFile_0;
registerStyles('vaadin-grid', $css_0, {moduleId: 'flow_css_mod_0'});
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === '520b0178a3a698488c4bc935e19a5f737878be1ea7e7a178edf49b1c87b291f0') {
    pending.push(import('./chunks/chunk-e5abc18231b55af0b9b5dc763de1082137fdaf8778055ac7b1fd5ff1b9157bd5.js'));
  }
  if (key === '24ca1776bab6c3a5c8239b9f8dabdbe9d4788c85ad545251985d4d225d212780') {
    pending.push(import('./chunks/chunk-700cc24417ded685cf8e76e348b57a86ba91becbf64ddd1f3be6c8fd7586a86e.js'));
  }
  if (key === '815fa28083b4e3c87a450a1a4dfc32ad74781edb3812ba47bde494b28d95d920') {
    pending.push(import('./chunks/chunk-50c35932b32cfb5d33df4a420b70bdc7bc635580c189510908be4b37a1c0f5b5.js'));
  }
  if (key === '59be84b6500fe29b58597096798216f6cab65b9a02eb74a69d0c6a9925375c5e') {
    pending.push(import('./chunks/chunk-700cc24417ded685cf8e76e348b57a86ba91becbf64ddd1f3be6c8fd7586a86e.js'));
  }
  if (key === '1c0c0fbea467ecb80ed6fe8c2af3ba640f6e90c6ba8ca9c7818cacf8c7959577') {
    pending.push(import('./chunks/chunk-700cc24417ded685cf8e76e348b57a86ba91becbf64ddd1f3be6c8fd7586a86e.js'));
  }
  if (key === '08b19bb5e72b05e5f6da2daccbc8d1f6bb2e5480761e088b1e87295d1c23f257') {
    pending.push(import('./chunks/chunk-d848996b8a61cbcca02fb346a1db39c757cdb5a30cf76959d8f56af0fb4173d4.js'));
  }
  if (key === '01a90516fe9797a830969eb9b201385a89de02c56b061b71c422ff4ecb7fb776') {
    pending.push(import('./chunks/chunk-700cc24417ded685cf8e76e348b57a86ba91becbf64ddd1f3be6c8fd7586a86e.js'));
  }
  if (key === 'dfd79aa7b75f6d72f65163c2cc2545b549e4748db106282d1a9925c65e4787cd') {
    pending.push(import('./chunks/chunk-d848996b8a61cbcca02fb346a1db39c757cdb5a30cf76959d8f56af0fb4173d4.js'));
  }
  if (key === '397c75c5f11d39305ddf953421411a78b999bc25d4521d5ee50983f33f618a41') {
    pending.push(import('./chunks/chunk-e5abc18231b55af0b9b5dc763de1082137fdaf8778055ac7b1fd5ff1b9157bd5.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}