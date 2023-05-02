
const btnLogin = document.querySelector('.btn-log');
const loginView = document.querySelector('.login-view');
const footer = document.querySelector('footer');
const signinBtn = document.querySelector('.signin-btn');
const signupBtn = document.querySelector('.signup-btn');
const formBox = document.querySelector('.form-box');
const btnBack1 = document.querySelector('.btn-back1');
const btnBack2 = document.querySelector('.btn-back2');
const accBtn = document.querySelector('.btn-acc');
const accView = document.querySelector('.account-view');
const scoreView = document.querySelector('.s-view-slitherlink')
btnLogin.addEventListener('click', () =>{
    console.log('log');
    loginView.style.zIndex = 3;
    loginView.style.opacity = 1;
    footer.style.display = 'none';
    scoreView.style.display = 'none';
});

btnBack1.addEventListener('click', () =>{
    console.log('back1');
    loginView.style.opacity = 0;
    loginView.style.zIndex = 0;
    footer.style.display = 'block';
    scoreView.style.display = 'flex';
});
btnBack2.addEventListener('click', () =>{
    console.log('back2');
    footer.style.display = 'block';
    scoreView.style.display = 'flex';
    accView.style.opacity = 0;
    accView.style.zIndex = 0;
});

accBtn.addEventListener('click', () =>{
    console.log('account');
    accView.style.zIndex = 3;
    accView.style.opacity = 1;
    footer.style.display = 'none';
    scoreView.style.display = 'none';
})



signupBtn.addEventListener('click', ()=>{
    formBox.classList.add('active');
    loginView.classList.add('active');
    console.log('formBox +.active loginView +.active');

});

signinBtn.addEventListener('click', ()=>{
    formBox.classList.remove('active');
    loginView.classList.remove('active');
    console.log('formBox -.active loginView -.active');
});







