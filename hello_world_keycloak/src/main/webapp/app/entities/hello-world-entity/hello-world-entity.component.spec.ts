/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import HelloWorldEntity from './hello-world-entity.vue';
import HelloWorldEntityService from './hello-world-entity.service';
import AlertService from '@/shared/alert/alert.service';

type HelloWorldEntityComponentType = InstanceType<typeof HelloWorldEntity>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('HelloWorldEntity Management Component', () => {
    let helloWorldEntityServiceStub: SinonStubbedInstance<HelloWorldEntityService>;
    let mountOptions: MountingOptions<HelloWorldEntityComponentType>['global'];

    beforeEach(() => {
      helloWorldEntityServiceStub = sinon.createStubInstance<HelloWorldEntityService>(HelloWorldEntityService);
      helloWorldEntityServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          helloWorldEntityService: () => helloWorldEntityServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        helloWorldEntityServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(HelloWorldEntity, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(helloWorldEntityServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.helloWorldEntities[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: HelloWorldEntityComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(HelloWorldEntity, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        helloWorldEntityServiceStub.retrieve.reset();
        helloWorldEntityServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        helloWorldEntityServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeHelloWorldEntity();
        await comp.$nextTick(); // clear components

        // THEN
        expect(helloWorldEntityServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(helloWorldEntityServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
