/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import HelloWorldEntityUpdate from './hello-world-entity-update.vue';
import HelloWorldEntityService from './hello-world-entity.service';
import AlertService from '@/shared/alert/alert.service';

type HelloWorldEntityUpdateComponentType = InstanceType<typeof HelloWorldEntityUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const helloWorldEntitySample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<HelloWorldEntityUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('HelloWorldEntity Management Update Component', () => {
    let comp: HelloWorldEntityUpdateComponentType;
    let helloWorldEntityServiceStub: SinonStubbedInstance<HelloWorldEntityService>;

    beforeEach(() => {
      route = {};
      helloWorldEntityServiceStub = sinon.createStubInstance<HelloWorldEntityService>(HelloWorldEntityService);
      helloWorldEntityServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          helloWorldEntityService: () => helloWorldEntityServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(HelloWorldEntityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.helloWorldEntity = helloWorldEntitySample;
        helloWorldEntityServiceStub.update.resolves(helloWorldEntitySample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(helloWorldEntityServiceStub.update.calledWith(helloWorldEntitySample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        helloWorldEntityServiceStub.create.resolves(entity);
        const wrapper = shallowMount(HelloWorldEntityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.helloWorldEntity = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(helloWorldEntityServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        helloWorldEntityServiceStub.find.resolves(helloWorldEntitySample);
        helloWorldEntityServiceStub.retrieve.resolves([helloWorldEntitySample]);

        // WHEN
        route = {
          params: {
            helloWorldEntityId: '' + helloWorldEntitySample.id,
          },
        };
        const wrapper = shallowMount(HelloWorldEntityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.helloWorldEntity).toMatchObject(helloWorldEntitySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        helloWorldEntityServiceStub.find.resolves(helloWorldEntitySample);
        const wrapper = shallowMount(HelloWorldEntityUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
