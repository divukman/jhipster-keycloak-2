export interface IHelloWorldEntity {
  id?: number;
  name?: string | null;
  testValue?: boolean | null;
}

export class HelloWorldEntity implements IHelloWorldEntity {
  constructor(
    public id?: number,
    public name?: string | null,
    public testValue?: boolean | null,
  ) {
    this.testValue = this.testValue ?? false;
  }
}
